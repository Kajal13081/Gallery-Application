package com.example.gallery

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.databinding.FragmentFolderBinding

class FolderFragment : Fragment() {
    private lateinit var binding: FragmentFolderBinding
    private lateinit var folderRecyclerViewAdapter: FolderRecyclerViewAdapter
    private lateinit var images: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_folder, container, false)
        loadImages(ImagesGallery.SortOrder.Modified)
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return binding.root
    }
    // Sort option
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.dateModifiedSorting ->
            {
                loadImages(ImagesGallery.SortOrder.Modified)
            }
            R.id.dateSorting ->{
                loadImages(ImagesGallery.SortOrder.Date)

            }
            R.id.nameSorting -> {
                loadImages(ImagesGallery.SortOrder.Name)
            }
            R.id.sizeSorting ->{
                loadImages(ImagesGallery.SortOrder.Size)
            }
        }
        return true
    }


    private fun loadImages(sortOrder: ImagesGallery.SortOrder) {
        binding.folderRecyclerView.setHasFixedSize(true)
        val gridLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        binding.folderRecyclerView.layoutManager = gridLayoutManager

        images = ImagesGallery.listOfSortedImages(requireContext(),sortOrder)

    //    images = ImagesGallery.listOfImage(requireContext())

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