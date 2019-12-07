package com.prattham.dogsapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.prattham.dogsapp.R
import com.prattham.dogsapp.databinding.ItemDogBinding
import com.prattham.dogsapp.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogsViewHolder>(), DogClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //  val view = inflater.inflate(R.layout.item_dog, parent, false)
        val view =
            DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)
        return DogsViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogsViewHolder, position: Int) {
//        with(holder.view) {
//            name.text = dogsList[position].dogBreed
//            lifespan.text = dogsList[position].lifeSpan
//            setOnClickListener {
//                val action = ListFragmentDirections.actionListFragmentToDetailFragment()
//                action.dogUuid = dogsList[position].uuid
//                Navigation.findNavController(it)
//                    .navigate(action)
//            }
//            imageView.loadImage(
//                dogsList[position].imageUrl, getProgressDrawable(imageView.context)
//            )
//        }

        //with DataBinding
        with(holder.view) {
            dog = dogsList[position]
            listener = this@DogsListAdapter
        }
    }

    fun updateDogsList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    class DogsViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)

    override fun onDogClicked(v: View) {
        val uuid = v.dogId.text.toString().toInt()
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        action.dogUuid = uuid
        Navigation.findNavController(v)
            .navigate(action)
    }

}