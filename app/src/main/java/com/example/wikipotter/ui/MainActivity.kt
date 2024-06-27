package com.example.wikipotter.ui

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikipotter.R
import com.example.wikipotter.utis.BlurBuilder
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), FilterListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var rvCharaters: RecyclerView
    private lateinit var adapter: CharactersAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //para activar la barra del menu
        setSupportActionBar(findViewById(R.id.toolbar))

        //Cambia el color de la status bar para que sea del mismo que la toolbar
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        //Para poner el fondo borroso
        val rootLayout = findViewById<ConstraintLayout>(R.id.activityMain)
        val background = resources.getDrawable(R.drawable.fondo_hogwarts, null)
        val blurredBackground = BlurBuilder.blur(this, background)
        rootLayout.background = BitmapDrawable(resources, blurredBackground)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //obtengo la instancia del firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        //checkeo que el usuario este en el firebase
        checkUser()
        //Obtiene el mail del usuario
        val email = firebaseAuth.currentUser!!.email!!
        //Setea el reciclerView y el adapter
        setRVA(email)


        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.characters2.observe(this){
            adapter.update(it)
        }


        viewModel.init(this)

    }

    private fun setRVA(email: String){
        //set del reciclerView y del adapter
        rvCharaters = findViewById(R.id.rvCharaters)
        rvCharaters.layoutManager = GridLayoutManager(this, 2)
        adapter=CharactersAdapter(email)
        rvCharaters.adapter = adapter
    }

    private fun checkUser(){
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            //si el usuario no esta, llamo a la pantalla del login para que ingrese
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflar el menu
        menuInflater.inflate(R.menu.main_menu, menu)

        // Configurar la barra de acción
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setLogo(R.drawable.logo_harry_potter)
        }

        //Obtengo los elementos del menu
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView

        //Que define lo que pasa cuando apretamos en el search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Restaurar la lista original si la búsqueda está vacía
                    adapter.restoreOriginalList()
                } else {
                    // Filtra la lista según el texto de búsqueda
                    adapter.filter(newText ?: "")
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                // Crear una instancia del fragmento de filtro
                val filterFragment = FilterBottomSheetFragment()
                // Establece un listener para recibir eventos de filtro
                filterFragment.setFilterListener(this)
                // Muestra el fragmento de filtro utilizando el FragmentManager
                filterFragment.show(supportFragmentManager, FilterBottomSheetFragment.TAG)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onFilterApplied(houses: List<String>, roles: List<String>) {
        viewModel.appliedFilter(houses,roles)
    }

    override fun clearFilters() {
        viewModel.restoreOriginalList()
    }

    override fun setFilters(houses: Map<String, Boolean>, roles: Map<String,Boolean>){
        viewModel.setFilters(houses,roles)
    }

    override fun getHouseFilters():Map<String,Boolean>{
        return viewModel.getHouseFilters()
    }

    override fun getRolesFilters():Map<String,Boolean>{
        return viewModel.getRolesFilters()
    }

    override fun seeFavorites() {
        viewModel.seeFavorites(firebaseAuth.currentUser!!.email!!)
    }

}