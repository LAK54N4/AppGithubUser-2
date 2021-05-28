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


        fun loadCategories(adapterCategory: CategoryAdapter, context: Context) {
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
                        }
                        adapterCategory.notifyDataSetChanged()
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

                            if (documentSnapshot.get("view_type") as Long == 0L) 