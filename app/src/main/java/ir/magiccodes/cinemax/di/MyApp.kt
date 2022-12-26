package ir.magiccodes.cinemax.di

import android.app.Application
import androidx.room.Room
import ir.magiccodes.cinemax.model.db.AppDatabase
import ir.magiccodes.cinemax.model.net.createApiService
import ir.magiccodes.cinemax.model.repository.RepositoryImpl
import ir.magiccodes.cinemax.model.repository.Repository
import ir.magiccodes.cinemax.ui.feature.detail.DetailFragmentViewModel
import ir.magiccodes.cinemax.ui.feature.home.HomeFragmentViewModel
import ir.magiccodes.cinemax.ui.feature.search.SearchFragmentViewModel
import ir.magiccodes.cinemax.ui.feature.see_all.SeeAllFragmentViewModel
import ir.magiccodes.cinemax.ui.feature.trailers.TrailersFragmentViewModel
import ir.magiccodes.cinemax.ui.feature.wishlist.WishlistFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val myModules = module {

            single { createApiService() }

            single { Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "app_dataBase.db")
                .fallbackToDestructiveMigration()
                .build() }

            single<Repository> { RepositoryImpl(get(), get<AppDatabase>().movieDao())}

            viewModel{ HomeFragmentViewModel(get()) }
            viewModel{ SeeAllFragmentViewModel(get()) }
            viewModel{ DetailFragmentViewModel(get()) }
            viewModel{ WishlistFragmentViewModel(get<AppDatabase>().movieDao()) }
            viewModel{ SearchFragmentViewModel(get()) }
            viewModel{ TrailersFragmentViewModel(get()) }
        }


        startKoin {
            androidContext(this@MyApp)
            modules(myModules)
        }
    }
}