package com.example.wikipotter.ui

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.wikipotter.R
import com.example.wikipotter.data.CharacterDataSource
import com.example.wikipotter.data.CharacterRepository
import com.example.wikipotter.utis.BlurBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterActivity : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var imagen: ImageView
    lateinit var aliases: TextView
    lateinit var species: TextView
    lateinit var house: TextView
    lateinit var dateOfBirth: TextView
    lateinit var ancestry: TextView
    lateinit var eyesColour: TextView
    lateinit var hairColour: TextView
    lateinit var wand: TextView
    lateinit var patronus: TextView
    lateinit var hogwartsRole: TextView
    lateinit var actor: TextView
    lateinit var pb: ProgressBar
    lateinit var vm: CharacterViewModel
    lateinit var ib: ImageButton
    lateinit var backBtn: Button
    private val charcRepo: CharacterRepository = CharacterRepository()
    private var isFavorite: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_character)

        //Para poner el fondo borroso
        val rootLayout = findViewById<ConstraintLayout>(R.id.activityCharacter)
        val background = resources.getDrawable(R.drawable.fondo_hogwarts, null)
        val blurredBackground = BlurBuilder.blur(this, background)
        rootLayout.background = BitmapDrawable(resources, blurredBackground)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityCharacter)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        vm = ViewModelProvider(this)[CharacterViewModel::class.java]

        name = findViewById(R.id.txtName)
        imagen = findViewById(R.id.imgChar)
        aliases = findViewById(R.id.txtAliases)
        species = findViewById(R.id.txtSpecies)
        house = findViewById(R.id.txtHouse)
        dateOfBirth = findViewById(R.id.txtDateOfBirth)
        ancestry = findViewById(R.id.txtAncestry)
        eyesColour = findViewById(R.id.txtEyeColour)
        hairColour = findViewById(R.id.txtHairColour)
        wand = findViewById(R.id.txtWand)
        patronus = findViewById(R.id.txtPatronus)
        hogwartsRole = findViewById(R.id.txtHogwartsRole)
        actor = findViewById(R.id.txtActor)
        pb = findViewById(R.id.pbChar)
        ib = findViewById(R.id.btn_favorite)
        backBtn = findViewById(R.id.backBtn)

        //Extraigo el id y el gmail del intent
        val id = intent.getStringExtra("id")!!
        val email = intent.getStringExtra("email")!!

        //Evaluo mediante una rutina si el character esta en favoritos
        CoroutineScope(Dispatchers.IO).launch {
            isFavorite = charcRepo.getChctFav(email, id)
            Log.d("DEMO_APIS","Favorite?" + isFavorite)
            withContext(Dispatchers.Main) {
                ib.isSelected = isFavorite
            }
        }

        vm.characters2.observe(this){
            name.text = it.name
            Glide.with(this).load(it.image).into(imagen)
            aliases.text = "Alias: ${it.alternes_name.joinToString(", ")}"
            species.text = "Species: ${it.species}"
            house.text = "House: ${it.house}"
            if(it.dateOfBirth== "null"){dateOfBirth.text = " "
            } else { dateOfBirth.text = "Date of Birth: ${it.dateOfBirth}"}
            ancestry.text = "Ancestry: ${it.ancestry}"
            eyesColour.text = "Eye Colour: ${it.eyeColour}"
            hairColour.text = "Hair Colour: ${it.hairColour}"
            var textwand: String
            if(it.wand.wood!=""){
                textwand = it.wand.wood
                if (it.wand.core!=""){
                    textwand += ", ${it.wand.core}"
                }
            }else if (it.wand.core!=""){
                textwand = it.wand.core
            } else textwand = ""
            wand.text = "Wand: ${textwand}"
            patronus.text = "Patronus: ${it.patronus}"
            if (it.hogwartsStudent){
                hogwartsRole.text = "Role: Student"
            } else if(it.hogwartsStaff){ hogwartsRole.text = "Role: Staff"}
            else {hogwartsRole.text = "Role: "}
            actor.text = it.actor
            pb.visibility = View.INVISIBLE
        }

        pb.visibility = View.VISIBLE
        vm.init(id,this)

        //Le digo al boton que debe hacer
        ib.setOnClickListener {
            Log.d("DEMO_APIS", "Estado en onClick:" + isFavorite)
            CoroutineScope(Dispatchers.IO).launch {
                if (isFavorite) {//Evalua el estado, si se encuentra en favoritos o no
                    charcRepo.removeChrctFav(email, id)
                } else {
                    charcRepo.setChrctFav(email, vm.characters2.value!!)
                }
            }
            ib.isSelected = !ib.isSelected //Cambia la estrella
            isFavorite = !isFavorite // Cambiar el estado
        }

        backBtn.setOnClickListener{
            onBackPressed()
        }
    }
}