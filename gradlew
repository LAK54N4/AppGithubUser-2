package com.laksana.kemirimall

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.laksana.kemirimall.adapter.CategoryAdapter
import com.laksana.kemirimall.adapter.HomePageAdapter
import com.laksana.kemirimall.model.*

object DBqueries {

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val categoryModelList: ArrayList<CategoryModel> = ArrayList()
        val homePageModelList: ArrayList<HomePageModel> = ArrayList()

        val lists: ArrayList<ArrayList<HomePageModel>> = ArrayList()
        val loadedCategoriesName: ArrayList<String> = ArrayList()


        fun loadCategories(adapterCategory: CategoryAdapter, context: Context?) {
            db.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (documentSnapshot: QueryDocumentSnapshot in it.result!!) {
                            categoryModelList.add(
                                CategoryModel(
                                    documentSnapshot.get("icon").toString(),
                                    documentSnapshot.get("categoryName").toString()
                                )
                            )
                            adapterCategory.notifyDataSetChanged()
                        }
                        //adapterCategory.notifyDataSetChanged()
                    } else {
                        val error = it.exception!!.message
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        fun loadFragmentData(adapter: HomePageAdapter, context: Context, index: Int, categoryName: String ) {
            db.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (documentSnapshot: QueryDocumentSnapshot in it.result!!) {
                            if (documentSnapshot.get("view_type") as Long == 0L) {
                                val sliderModelList: ArrayList<SliderModel> = ArrayList()
                                val noOfBanners: Long = documentSnapshot.get("no_of_banners") as Long
                                for (x in 1 until noOfBanners + 1 step 1) {
                                    Log.d(
                                        "exist",
                                        "DocumentSnapshot data: ${documentSnapshot.get("banner_$x")} "
                                    )
                                    sliderModelList.add(
                                        SliderModel(
                                            documentSnapshot.get("banner_$x").toString(),
                                            documentSnapshot.get("banner_$x" + "_background").toString()
                                            //Log.d("exist", "DocumentSnapshot data: ${documentSnapshot.get("banner_$x")} ")
                                            //    .toString(),
                                            //Log.d("exist", "DocumentSnapshot data: ${documentSnapshot.get("banner_$x"+"_background")} ")
                                            //    .toString()
                                        )
                                    )
                                }
                                lists[index].add(HomePageModel(0, sliderModelList))
                            }
                            else if (documentSnapshot.get("view_type") as Long == 1L) {
                                lists[index].add(
                                    HomePageModel(1,
                                        documentSnapshot.get("strip_ad_banner").toString(),
                                        documentSnapshot.get("background").toString()
                                    )
                                )
                            }
                            else if (documentSnapshot.get("view_type") as Long == 2L) {

                                val viewAllProductList: ArrayList<WishlistModel> = ArrayList()
                                val horizontalProductScrollModeList: ArrayList<HorizontalProductScrollModel> = ArrayList()
                                //val numberOfProducts = documentSnapshot.get("number_of_product") as Long
                                //val numberOfProduct = documentSnapshot.get("no_of_products") as Long
                                val numberOfProducts = 3
                                for (x in 1 until numberOfProducts + 1 step 1) {
                                    //for (x in 1 until 4 + 1 step 1) {
                                    horizontalProductScrollModeList.add(
                                        HorizontalProductScrollModel(
                                            documentSnapshot.get("product_ID_$x").toString(),
                                            documentSnapshot.get("