package my.edu.tarumt.epf.ui.profile

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import my.edu.tarumt.epf.R
import my.edu.tarumt.epf.databinding.FragmentProfileBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), MenuProvider {
    var _binding: FragmentProfileBinding? = null
    val binding get() = _binding!!


    //Implicit intent
    private val getPhoto = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri != null){
            binding.imageViewProfile.setImageURI(uri)
        }
    }

    lateinit var SharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        //Add support to Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewProfile.setOnClickListener{
            getPhoto.launch("image/*")
        }

        //TODO Read profile picture
        val image = readProfilePicture()
        if(image == null){
            binding.imageViewProfile.setImageResource(R.drawable.baseline_account_box_24)
        }
        else{
            binding.imageViewProfile.setImageBitmap(image)
        }
        //TODO Read Profile INFO
        SharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        val name = SharedPref.getString(getString(R.string.name),
        getString(R.string.nav_header_title))
        val email = SharedPref.getString(getString(R.string.email),
        getString(R.string.nav_header_subtitle))

        if(name != null){
            binding.editTextName.setText(name)
            binding.editTextEmailAddress.setText(email)
        }
        else{
            binding.editTextName.setText("Android Studio")
            binding.editTextEmailAddress.setText("andriod.studio@android.com")
        }




    }

    private fun saveProfilePicture(view: View) {
        val filename = "profile.png"
        val file = File(this.context?.filesDir, filename)
        val image = view as ImageView

        val bd = image.drawable as BitmapDrawable
        val bitmap = bd.bitmap
        val outputStream: OutputStream

        try{
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
        }catch (e: FileNotFoundException){
            e.printStackTrace()
        }
    }

    private fun readProfilePicture(): Bitmap? {
        val filename = "profile.png"
        val file = File(this.context?.filesDir, filename)

        if(file.isFile){
            try{
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                return bitmap
            }catch (e: FileNotFoundException){
                e.printStackTrace()
            }
        }
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.profile_menu, menu)
        menu.findItem(R.id.action_about).isVisible = false
        menu.findItem(R.id.action_settings).isVisible = false

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
       //TODO save Profile Picture
        if (menuItem.itemId == R.id.action_save) {
            //TODO: Save profile Info
            saveProfilePicture(binding.imageViewProfile)
            Toast.makeText(requireContext(), getString(R.string.profile_save), Toast.LENGTH_SHORT).show()
        } else if (menuItem.itemId == android.R.id.home) {
            findNavController().navigateUp()
        }

        //TODO Save Profile Info
        val name = binding.editTextName.text.toString()
        val email = binding.editTextEmailAddress.text.toString()

        with(SharedPref.edit()){
            putString(getString(R.string.name), name)
            putString(getString(R.string.email), email)
            apply()
        }

        val navigationView = requireActivity().findViewById<View>(R.id.nav_view) as NavigationView
        val view = navigationView.getHeaderView(0)
        val profilePic = view.findViewById<ImageView>(R.id.profileImg)
        val textViewName = view.findViewById<TextView>(R.id.profileName)
        val textViewEmail = view.findViewById<TextView>(R.id.profileEmail)

        profilePic.setImageBitmap(readProfilePicture())
        textViewName.text = name
        textViewEmail.text = email

        return true
    }
}