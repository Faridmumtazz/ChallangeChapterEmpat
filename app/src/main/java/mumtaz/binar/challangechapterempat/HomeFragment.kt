package mumtaz.binar.challangechapterempat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.edit_custom_dialog.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var prefs: SharedPreferences
    private var mdbNew: StatusDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mdbNew = StatusDatabase.getInstance(requireContext())

        prefs = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)

        val tvnama = "Welcome, ${prefs.getString("nama", "")}"
        tv_usernamee.text = tvnama

        getDataStatus()

//============================ Untuk ikon tambah ====================================================
        tv_keluar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Keluar")
                .setMessage("Yakin keluar?")
                .setIcon(R.drawable.binar)
                .setPositiveButton("Ya") { p0, p1 ->
                    prefs.edit().clear().apply()
                    it.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }.setNegativeButton("Tidak") { p0, p1 ->
                }.show()
        }

//=======================Untuk tambah data di custom dialog==========================================

        fab_add.setOnClickListener {
            val customDialog =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null, false)
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(customDialog)
                .create()

//======================= Untuk eksekusi data di custom dialog =====================================
            customDialog.btn_tambah.setOnClickListener {
                GlobalScope.async {
                    val nama = customDialog.et_tdl_nama.text.toString()
                    val status = customDialog.et_tdl_status.text.toString()

                    val hasil = mdbNew?.statusDao()?.insertStatus(Status(null, nama, status))

                    activity?.runOnUiThread {
                        if (hasil != 0.toLong()) {
                            Toast.makeText(requireContext(), "Succes", Toast.LENGTH_LONG).show()
                            Log.i("Success", hasil.toString())
                            alertDialog.dismiss()
                        } else {
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
                            Log.i("Failed", hasil.toString())
                        }
                    }
                }

            }
            alertDialog.show()
        }
    }
//================================== Untuk menampilkan data ========================================
    fun getDataStatus (){
        rv_binar.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        GlobalScope.launch {
            val listdata = mdbNew?.statusDao()?.getAllStatus()

            activity?.runOnUiThread {
                listdata.let {
                    val adapt = AdapterStatus(it!!)
                    rv_binar.adapter = adapt
                }
            }
        }


    }


//    =========================================================================================
    override fun onResume() {
        super.onResume()
        getDataStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        StatusDatabase.destroyInstance()
    }

}

    



