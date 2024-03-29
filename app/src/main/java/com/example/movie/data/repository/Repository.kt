package com.example.movie.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    databaseSource: DatabaseSource


) {

    val remote = remoteDataSource
    val local = databaseSource

}