package ir.magiccodes.cinemax.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.databinding.ActivityMainBinding
import ir.magiccodes.cinemax.ui.feature.home.HomeFragment
import ir.magiccodes.cinemax.ui.feature.wishlist.WishlistFragment
import ir.magiccodes.cinemax.ui.feature.search.SearchFragment
import ir.magiccodes.cinemax.ui.feature.trailers.TrailersFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigation()

    }

    private fun bottomNavigation(){
        firstRun()
        binding.bottomNavigation.setOnItemSelectedListener {

            when (it) {
                R.id.menu_home -> {
                    replaceFragment(HomeFragment())
                }

                R.id.menu_search -> {
                    replaceFragment(SearchFragment())
                }

                R.id.menu_trailers -> {
                    replaceFragment(TrailersFragment())
                }

                R.id.menu_saved -> {
                    replaceFragment(WishlistFragment())
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView , fragment)
        transaction.commit()
    }
    private fun firstRun(){
        replaceFragment(HomeFragment())
        binding.bottomNavigation.setItemSelected(R.id.menu_home, true)
    }
}

