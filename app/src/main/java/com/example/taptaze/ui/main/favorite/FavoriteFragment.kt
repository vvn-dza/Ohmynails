package com.example.taptaze.ui.main.favorite

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.taptaze.R
import java.util.*

class FavoritesFragment : Fragment() {

    private var phone: String? = null
    private var selectedDate: String? = null
    private var time: String? = null
    private val timings = arrayOf(
        "10 A.M - 11 A.M",
        "11.30 A.M - 12.30 P.M",
        "2 P.M - 3 P.M",
        "4 P.M - 5 P.M",
        "6 P.M - 7 P.M"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        val nameEditText = view.findViewById<EditText>(R.id.editTextTextPersonName)
        val diseaseEditText = view.findViewById<EditText>(R.id.editTextTextPersonName2)
        val phoneEditText = view.findViewById<EditText>(R.id.editTextPhone)
        val dateButton = view.findViewById<Button>(R.id.button2)
        val bookButton = view.findViewById<Button>(R.id.button)
        val spinner = view.findViewById<Spinner>(R.id.spinner)

        // Spinner setup
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, timings
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                time = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Date picker with previous dates disabled
        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    dateButton.text = selectedDate
                }, year, month, day
            )
            datePickerDialog.datePicker.minDate = calendar.timeInMillis // Disable past dates
            datePickerDialog.show()
        }

        // Booking appointment
        bookButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            phone = phoneEditText.text.toString().trim()
            val disease = diseaseEditText.text.toString().trim()

            if (name.isEmpty() || phone!!.isEmpty() || disease.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDate == null) {
                Toast.makeText(requireContext(), "Select a valid date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone!!.length == 10) {
                Toast.makeText(requireContext(), "Appointment booked successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Enter valid phone number", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}