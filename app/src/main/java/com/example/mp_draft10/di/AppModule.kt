package com.example.mp_draft10.di

import android.content.Context
import com.example.mp_draft10.BuildConfig
import com.example.mp_draft10.auth.AuthRepository
import com.example.mp_draft10.auth.AuthRepositoryImpl
import com.example.mp_draft10.auth.util.ApplicationFirebaseAuth
import com.example.mp_draft10.commons.Network
import com.example.mp_draft10.commons.NetworkConnectivity
import com.example.mp_draft10.database.DatabaseRepository
import com.example.mp_draft10.database.DatabaseRepositoryImpl
import com.example.mp_draft10.remote.ApiService
import com.example.mp_draft10.repository.NetworkDataSource
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
//@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth:FirebaseAuth): AuthRepository {
        return  AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesDatabaseRepositoryImpl(): DatabaseRepository {
        return DatabaseRepositoryImpl()
    }

    /**
     * API FUNCTIONS
     */

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): ApplicationFirebaseAuth =
        app as ApplicationFirebaseAuth

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext app: Context): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideRetrofit(): ApiService = provideRetrofitApi()

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity =
        Network(context)

    @Singleton
    @Provides
    fun providesNetworkDataSource(apiService: ApiService): NetworkDataSource =
        NetworkDataSource(apiService)

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private fun provideRetrofitApi(
        baseUrl: HttpUrl = BuildConfig.BASE_URL.toHttpUrl(),
        client: () -> OkHttpClient = { makeOkHttpClient() },
    ): ApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client())
        .build().create(ApiService::class.java)


    private fun makeOkHttpClient(
        logging: () -> Interceptor = { loggingInterceptor() },
        authorization: () -> Interceptor = { authorizationInterceptor() },
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging())
            .addInterceptor(authorization())
            .build()

    private fun authorizationInterceptor() = Interceptor {
        val url: HttpUrl = it.request().url
            .newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY)
            .build()
        val request: Request = it.request().newBuilder().url(url).build()
        it.proceed(request)
    }

    private fun loggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
}
