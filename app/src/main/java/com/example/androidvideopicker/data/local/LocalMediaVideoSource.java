package com.example.androidvideopicker.data.local;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.androidvideopicker.data.entity.VideoAlbumName;
import com.example.androidvideopicker.data.entity.VideoMedia;
import com.example.androidvideopicker.data.entity.VideoRvItem;

import java.util.ArrayList;

public
class LocalMediaVideoSource {


    /*
    사용되는 프로젝션 목록
    api29이상
    _ID: 51
    DATA: /storage/emulated/0/Naver/비디오제목.mp4
    MIME_TYPE: video/mp4
    WIDTH: 1280
    HEIGHT: 720
    DURATION: 190592
    SIZE: 52669878
    BUCKET_DISPLAY_NAME: Naver
    DISPLAY_NAME: 비디오제목.mp4
    BUCKET_ID: 83226742
    DATE_ADDED: 1613044742
    TITLE: 비디오제목
 */

    //-------------------------------------------------------------------------------------------------
    //    안드로이드 Q(29) 미만일 경우에 디렉토리를 꺼내오는 프로젝션
    private final String[] UNDER_28_DIRECTORY_PROJECTION = {
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE
    };

    /**
     * 29미만일 경우 앨범명을 구하기 위한 코드이며 버켓아이디가 따로없어서
     * 전체경로에서 마지막 / 를 잘라서 앨범경로를 버켓아이디를 대체함
     * @param context 리졸버 사용하는 용도
     * @return 앨범 목록 리스트
     */
    public ArrayList<VideoAlbumName> getVideoDirectoryUnder28(Context context) {


        //        HashMap<String, String> returnMap = new HashMap<>();
        ArrayList<VideoAlbumName> returnList = new ArrayList<>();

//        비디오만 가져오는 유알아이호출
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

//      /storage/emulated/0/DCIM/Instagram/video/파일명.파일형식 으로 되어있고 마지막/을 잘라서 리스트에 없을경우 담아준다
//        파일의 위치가 중복되는지 확인하기 위해서 사용되고 리스트에 포함되어있으면 맵에 포함시켜준다
//        맵의 키는 pathVideo이다 왜냐 해당키가 고유한 값이기때문이다.
        ArrayList<String> checkDirectoryMediaList = new ArrayList<>();

//        파일명을 제외한 파일의 경로가 담기는 스트링 예시/storage/emulated/0/DCIM/Instagram/video
        String pathVideo;

//       파일이 담겨있는 바로 위의 디렉토리의 이름이 담기는 스트링 예시 /storage/emulated/0/DCIM/Instagram/video -> video
        String directoryName;

//        동영상 파일이 어떤형식인지를 체크하기 위해 사용하는 변수 우리는 mp4만 허용한다
//        String videoMimeType;

        Cursor cursor = context.getContentResolver().query(uri, UNDER_28_DIRECTORY_PROJECTION, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {

//                String pathData = cursor.getString(cursor.getColumnIndexOrThrow(under29Projection[0]));
//                /storage/emulated/0/DCIM/Instagram/video/파일명.파일형식 으로 되어있고 마지막/을 잘라서 리스트에 없을경우 담아준다

                if (checkDirectoryMediaList.contains(
                        cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_DIRECTORY_PROJECTION[0]))/*파일이 있는 전체경로*/
                                .substring(0,/*처음부터 잘라준다*/
                                        cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_DIRECTORY_PROJECTION[0])).lastIndexOf("/"))
//                        /* /의 마지막부분이 파일명.파일형식 이므로 마지막 /로 부터 앞부분이 전부다 경로여서 사용한다 */
                )) {
//                    포함되어있는경우는 맵에 추가를 하지않는다.
                } else {
//                    어레이리스트에 포함되어있지경우 다음 순서커서의 중복검사를 위해서 리스트에 추가해준다

//                    videoMimeType = cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_DIRECTORY_PROJECTION[1]));
//                    if (videoMimeType.equals("video/mp4")) {
                        pathVideo = cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_DIRECTORY_PROJECTION[0]))/*파일이 있는 전체경로*/
                                .substring(0,/*처음부터 마지막 / 까지 잘라준다*/
                                        cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_DIRECTORY_PROJECTION[0])).lastIndexOf("/"));
                        directoryName = pathVideo.substring(pathVideo.lastIndexOf("/") + 1);

                        checkDirectoryMediaList.add(pathVideo);
//                    returnMap.put(pathVideo, directoryName);
                        returnList.add(new VideoAlbumName(pathVideo, directoryName));
//                    }
                }
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return returnList;
    }


    //    ---------------------------------------------------------------------------------------------
//    안드로이드 Q(29) 이상일 경우에 디렉토리를 꺼내오는 프로젝션
    private final String[] UPPER_29_DIRECTORY_PROJECTION = {
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.MIME_TYPE

    };

    /**
     * 안드로이드 Q(29) 이상일 경우 -
     *
     * @param context 리졸버 호출 용도
     * @return 앨범 목록 리스트 VideoAlbumNameData = 비디오의 고유한 숫자값 , 비디오가 들어잇는 앨범명
     */
    public ArrayList<VideoAlbumName> getVideoDirectoryUpper29(Context context) {

        ArrayList<VideoAlbumName> returnList = new ArrayList<>();

//        비디오만 가져오는 유알아이호출
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

//        파일의 위치가 중복되는지 확인하기 위해서 사용되고 리스트에 포함되어있으면 맵에 포함시켜준다
//        맵의 키는 리스트의 사이즈이다 , pathVideo가 추가된다
        ArrayList<String> checkDirectoryMediaList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(uri, UPPER_29_DIRECTORY_PROJECTION, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {


//                맵에서 리스트로 변경하면서 주석처리
                if (checkDirectoryMediaList.contains(cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_DIRECTORY_PROJECTION[1])))) {
//                    포함되어있는경우는 맵에 추가를 하지않는다.
                } else {

//                    타입이 mp4일 경우만 넣는다
//                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_DIRECTORY_PROJECTION[2]));
//                    if (mimeType.equals("video/mp4")) {

//                    어레이리스트에 포함되어있지경우 다음 순서커서의 중복검사를 위해서 고유 디렉토리 아이디(BUCKET_ID) 를 리스트에 추가해준다
                        checkDirectoryMediaList.add(
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_DIRECTORY_PROJECTION[1]))
                        );

                        returnList.add(new VideoAlbumName(
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_DIRECTORY_PROJECTION[1])),
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_DIRECTORY_PROJECTION[0]))
                                )
                        );

//                    returnMap.put(String.valueOf(checkDirectoryMediaList.size()),
//                            cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_DIRECTORY_PROJECTION[0]))
//                    );
//                    }

                }
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return returnList;
    }


    //    //    안드로이드 Q(29) 이하일 경우에 비디오의 정보를 담기위해 꺼내오는 프로젝션
    String[] UNDER_28_PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.TITLE
            //                        MediaStore.MediaColumns.DURATION,
            //            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            //            MediaStore.MediaColumns.BUCKET_ID,
    };

    public ArrayList<VideoMedia> getVideoInfoUnder28(Context context) {
        ArrayList<VideoMedia> returnList = new ArrayList<>();


//        비디오만 가져오는 유알아이호출
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        //        파일명을 제외한 파일의 경로가 담기는 스트링 예시/storage/emulated/0/DCIM/Instagram/video
        String pathVideo;

//        컨텐츠유알아이를 가져오기 위해서 먼저 아이디값을 가져와서 변수로 만들어사용한다
        long id;

//        28이하일 경우 duration 컬럼을 써도되는거 같긴한데 확실하지 않아서 다른방법으로 도영상길이를 찾기
//        위해서 사용한다
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

//        맵에있는 리스트를 다시 꺼냈다가 담기 위해서 만든 리스트


        Cursor cursor = context
                .getContentResolver()
                .query(uri,
                        UNDER_28_PROJECTION,
                        null,
                        null,
                        MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {

//                mp4일 경우만 추가해준다
                String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[2]));
//                if (mimeType.equals("video/mp4")) {

                //                전체 데이터의 키로 추가해준다 무조건 다들어가게 된다
                id = cursor.getLong(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[0]));

                pathVideo = cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_DIRECTORY_PROJECTION[0]))/*파일이 있는 전체경로*/
                        .substring(0,/*처음부터 마지막 / 까지 잘라준다*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_DIRECTORY_PROJECTION[0])).lastIndexOf("/"));

                //                전체 데이터의 키로 추가해준다 무조건 다들어가게 된다
                returnList.add(
                        new VideoMedia(
                                uri.buildUpon().appendPath(String.valueOf(id)).build().toString(), /*contentUri*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[0])),/*id*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[1])),/*data*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[2])),/*mimeType*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[3])),/*width*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[4])),/*height*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[5])),/*size*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[6])),/*displayName*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[7])),/*dateAdded*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UNDER_28_PROJECTION[8])),/*title*/
                                getVideoDuration(context, uri.buildUpon().appendPath(String.valueOf(id)).build().toString(), retriever),/*duration*/
                                "NULL",
                                pathVideo/*bucketId*/

                        )
                );
//                }
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return returnList;
    }


    //    안드로이드 Q(29) 이상일 경우에 비디오의 정보를 담기위해 꺼내오는 프로젝션
    @RequiresApi(api = Build.VERSION_CODES.Q)
    String[] UPPER_29_PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.TITLE,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.BUCKET_ID,
//            MediaStore.MediaColumns.DATA,
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public ArrayList<VideoMedia> getVideoInfoUpper29(Context context) {
        ArrayList<VideoMedia> returnList = new ArrayList<>();

//        비디오만 가져오는 유알아이호출
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

//        컨텐츠유알아이를 가져오기 위해서 먼저 아이디값을 가져와서 변수로 만들어사용한다
        long id;

//        동영상길이를 만드는 변수
        long duration;
        long minutes = 0;
        long seconds;
        String secondsStr;
        String secs = "00";


        Cursor cursor = context
                .getContentResolver()
                .query(uri,
                        UPPER_29_PROJECTION,
                        null,
                        null,
                        MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {

                //                mp4일 경우만 추가해준다
                String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[1]));
//                if (mimeType.equals("video/mp4")) {

//                전체 데이터의 키로 추가해준다 무조건 다들어가게 된다
                id = cursor.getLong(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[0]));

//                동영상 길이를 구함
                try {
                    duration = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[8])));
                    minutes = (duration / 1000) / 60;
                    seconds = (duration / 1000) % 60;
                    secondsStr = Long.toString(seconds);
                    if (secondsStr.length() >= 2) {
                        secs = secondsStr.substring(0, 2);
                    } else {
                        secs = "0" + secondsStr;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    minutes = 00;
                }

                //                전체 데이터의 키로 추가해준다 무조건 다들어가게 된다
                returnList.add(
                        new VideoMedia(
                                uri.buildUpon().appendPath(String.valueOf(id)).build().toString(), /*contentUri*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[0])),/*id*/
                                "NULL",/*data*/
                                nullCheckVideoAspect(cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[1]))),/*mimeType*/
                                nullCheckVideoAspect(cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[2]))),/*width*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[3])),/*height*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[4])),/*size*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[5])),/*displayName*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[6])),/*dateAdded*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[7])),/*title*/
                                minutes + ":" + secs,/*duration*/
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[9])),
                                cursor.getString(cursor.getColumnIndexOrThrow(UPPER_29_PROJECTION[10]))/*bucketId*/
                        )
                );
//                }
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return returnList;
    }


    /**
     * 29미만일 경우 리턴 동영상 길이를 가져오는 코드
     *
     * @param context
     * @param uri
     * @param retriever
     * @return
     */
    private String getVideoDuration(Context context, String uri, MediaMetadataRetriever retriever) {
        try {
            retriever.setDataSource(context, Uri.parse(uri));
//        milliseconds = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

            long minutes = (Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000) / 60;
            long seconds = (Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000) % 60;
            String secondsStr = Long.toString(seconds);
            String secs;
            if (secondsStr.length() >= 2) {
                secs = secondsStr.substring(0, 2);
            } else {
                secs = "0" + secondsStr;
            }

            return minutes + ":" + secs;
        } catch (Exception e) {
            e.printStackTrace();
            return "00:00";
        }
    }

    /**
     * 중국기기들에서 비디오의 가로세로를 못가져와서 널체크 후
     * 해상도를 사용할 수도 있기떄문에 1024로 넣어서 리턴함
     *
     * @param target width or height
     * @return if null -> 1024 / else target
     */
    private String nullCheckVideoAspect(String target) {
        if (target == null) {
            target = "1024";
        }
        return target;
    }

}
