package com.example.gallery

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.databinding.FragmentFolderBinding
import com.example.gallery.databinding.FragmentScrollingBinding

class FolderFragment : Fragment() {
    private lateinit var binding: FragmentFolderBinding
    private lateinit var folderRecyclerViewAdapter: FolderRecyclerViewAdapter
    private lateinit var images: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_folder, container, false)
        loadImages()
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return binding.root
    }
    // Sort option
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }
    private fun loadImages() {
        binding.folderRecyclerView.setHasFixedSize(true)
        val gridLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        binding.folderRecyclerView.layoutManager = gridLayoutManager
        images = ImagesGallery.listOfImage(requireContext())

        folderRecyclerViewAdapter =
            FolderRecyclerViewAdapter(AlbumConverter.getAlbum(images), object :
                FolderRecyclerViewAdapter.AlbumListener {
                override fun onAlbumClick(list: List<String>) {
                    findNavController().navigate(
                        FolderFragmentDirections.actionFolderFragmentToPhotosFragment(
                            list.toTypedArray()
                        )
                    )
                }
            })
        binding.folderRecyclerView.adapter = folderRecyclerViewAdapter
    }
}