package com.example.qualitywash.ui.Screen



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(){
    Scaffold (


    ) {innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally


        ){
            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Bienvenido riz",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun preview(){
    HomeScreen()
}