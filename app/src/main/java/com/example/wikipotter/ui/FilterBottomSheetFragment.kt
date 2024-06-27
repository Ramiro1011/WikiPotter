package com.example.wikipotter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import com.example.wikipotter.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private var filterListener: FilterListener? = null

    private var houseFilters = mutableMapOf<String,Boolean>()
    private var rolFilters = mutableMapOf<String,Boolean>()

    companion object {
        //Unico para los logs
        const val TAG = "FilterBottomSheetFragment"
    }

    //set del listener
    fun setFilterListener(listener: FilterListener) {
        filterListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_filter, container, false)

        val house1CheckBox: CheckBox = view.findViewById(R.id.Gryffindor)
        val house2CheckBox: CheckBox = view.findViewById(R.id.Slytherin)
        val house3CheckBox: CheckBox = view.findViewById(R.id.Hufflepuff)
        val house4CheckBox: CheckBox = view.findViewById(R.id.Ravenclaw)
        val studentCheckBox: CheckBox = view.findViewById(R.id.student)
        val staffCheckBox: CheckBox = view.findViewById(R.id.staff)

        val applyFiltersButton: Button = view.findViewById(R.id.applyFiltersButton)
        val closeButton: Button = view.findViewById(R.id.closeButton)
        val favBtn: Button = view.findViewById(R.id.favBtn)
        val clearFiltersBtn: Button = view.findViewById(R.id.clearFiltersButton)

        //inicio los filtros
        initFilters(filterListener!!.getHouseFilters(),filterListener!!.getRolesFilters())

        //verifica si hay filtros activos
        updateFilterControls(house1CheckBox, house2CheckBox, house3CheckBox, house4CheckBox, studentCheckBox, staffCheckBox)

        //Que hacer cuando se selecciona un check.
        staffCheckBox.setOnCheckedChangeListener { _, isChecked ->
            studentCheckBox.isChecked = false
            studentCheckBox.isEnabled = !isChecked
        }
        studentCheckBox.setOnCheckedChangeListener { _, isChecked ->
            staffCheckBox.isChecked = false
            staffCheckBox.isEnabled = !isChecked

        }

        //Define lo que hace el boton aplicar filtros
        applyFiltersButton.setOnClickListener {

            //Asigna true o false dependiendo de que check este seleccionado
            houseFilters["Gryffindor"] = house1CheckBox.isChecked
            houseFilters["Slytherin"] = house2CheckBox.isChecked
            houseFilters["Hufflepuff"] = house3CheckBox.isChecked
            houseFilters["Ravenclaw"] = house4CheckBox.isChecked
            rolFilters["Student"] = studentCheckBox.isChecked
            rolFilters["Staff"] = staffCheckBox.isChecked

            //Filtra la lista de filtros eliminando los que son false
            val selectedHouses = houseFilters.filter { it.value }.map { it.key }
            val selectedRoles = rolFilters.filter { it.value }.map { it.key }

            //Aplica los filtros
            filterListener?.onFilterApplied(selectedHouses, selectedRoles)
            //Guarda los filtros seleccionados para mantenerlos
            filterListener?.setFilters(houseFilters,rolFilters)
            dismiss()
        }


        //cierra la pantalla de filtros
        closeButton.setOnClickListener {
            dismiss()
        }

        //muestra los favoritos
        favBtn.setOnClickListener{
            filterListener?.seeFavorites()
            dismiss()
        }

        //Limpia los filtros, muestra todos los personajes
        clearFiltersBtn.setOnClickListener{
            filterListener?.clearFilters()
            dismiss()
        }

        return view
    }

    private fun initFilters(houses:Map<String,Boolean>, roles:Map<String,Boolean>) {
        //inicializa los filtros cuando se crean o actualizan
        houseFilters.clear()
        rolFilters.clear()

        houseFilters.putAll(houses)
        rolFilters.putAll(roles)
    }

    private fun updateFilterControls(
        house1CheckBox: CheckBox,
        house2CheckBox: CheckBox,
        house3CheckBox: CheckBox,
        house4CheckBox: CheckBox,
        studentCheckBox: CheckBox,
        staffCheckBox: CheckBox
    ) {
        //Verifica si no hay checkBox seleccionados con anterioridad osea filtros que se hayan seleccionado
        //Mantiene consistencia de los filtros
        house1CheckBox.isChecked = houseFilters["Gryffindor"] ?: false
        house2CheckBox.isChecked = houseFilters["Slytherin"] ?: false
        house3CheckBox.isChecked = houseFilters["Hufflepuff"] ?: false
        house4CheckBox.isChecked = houseFilters["Ravenclaw"] ?: false
        studentCheckBox.isChecked = rolFilters["Student"] ?: false
        staffCheckBox.isChecked = rolFilters["Staff"] ?: false

        if (staffCheckBox.isChecked == true){
            studentCheckBox.isEnabled = false
        }
        else if (studentCheckBox.isChecked == true){
            staffCheckBox.isEnabled = false
        }
    }

}