package com.example.authentication.walkthroughscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.authentication.R

class onboardingitemadapter (private val onboardingItems: List<OnboardingItem>) :
    RecyclerView.Adapter<onboardingitemadapter.OnboardingItemViewHolder>() {


        inner class OnboardingItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
            private val imageOnboarding = view.findViewById<LottieAnimationView> (R.id.imageOnboarding)
            private val textTitle = view.findViewById<TextView>(R.id.textTitle)
            private val textDescription = view.findViewById<TextView>(R.id.textDescription)
            fun bind(onboardingItem: OnboardingItem) {
                imageOnboarding.setAnimation(onboardingItem.onboardingImage)
                textTitle.text = onboardingItem.title
                textDescription.text = onboardingItem.description
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingItemViewHolder {
        return OnboardingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container,
                parent,
                 false))
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    override fun onBindViewHolder(holder: OnboardingItemViewHolder, position: Int) {
        holder.bind(onboardingItems[position])

    }
}