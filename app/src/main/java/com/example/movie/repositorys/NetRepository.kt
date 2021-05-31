package com.example.movie.repositorys

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class NetRepository @Inject constructor(
    remoteDataSource: RemoteDataSource


) {

    val remote = remoteDataSource

}