package com.example.androidvideopicker.data.repository;

public
interface RepositoryReturnDataListener {
    void successReturn (String flag , Object object);
    void failReturn (String flag , Object object);
}
