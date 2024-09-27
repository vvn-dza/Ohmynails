package com.example.taptaze.ui.main.favorite

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        // Request SMS permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.SEND_SMS),
                0
            )
        }

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

        // Date picker
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
            datePickerDialog.show()
        }

        // Booking appointment
        bookButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            phone = phoneEditText.text.toString().trim()
            val disease = diseaseEditText.text.toString().trim()

            if (name.isEmpty() || phone.isNullOrEmpty() || disease.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone!!.length == 10 && phone!!.all { it.isDigit() }) {
                sendMessage(name, disease)
            } else {
                Toast.makeText(requireContext(), "Enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun sendMessage(name: String, disease: String) {
        try {
            val sms = SmsManager.getSmsManagerForSubscriptionId(SmsManager.getDefaultSmsSubscriptionId())
            sms.sendTextMessage(phone, null, "Appointment booked for $name with $disease on $selectedDate at $time.", null, null)

            Toast.makeText(requireContext(), "Appointment booked successfully", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to book appointment", Toast.LENGTH_LONG).show()
        }
    }
}
