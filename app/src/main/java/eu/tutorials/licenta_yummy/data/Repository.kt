package eu.tutorials.licenta_yummy.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import jakarta.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource
) {
    val remote = remoteDataSource
}