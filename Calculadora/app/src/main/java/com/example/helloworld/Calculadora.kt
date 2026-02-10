package com.example.helloworld


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helloworld.ui.theme.HelloWorldTheme



@Composable //cosas q se pueden pintar
fun Calculadora(modifier: Modifier = Modifier){
    var texto1 by remember { mutableStateOf("") }
    var texto2 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    var resultadoTxt by remember { mutableStateOf("")}
    val num1 = texto1.toIntOrNull()?: 0 // si el texto es null ponemos un 0
    val num2 = texto2.toIntOrNull()?: 0


    Column ( //columna vertical
        modifier = Modifier
            .fillMaxWidth()  // la columna ocupa todo el ancho
            .padding(16.dp)
    )
            {
        Row( // fila horizontal
            modifier = Modifier
                .padding(16.dp)

        )
        {
            TextField(
                value = texto1,
                onValueChange = { texto1 = it },
                label = { Text("primer número") },
                placeholder = { Text("Escribe el primer número")},
                modifier = Modifier.weight(1f)
            )
            Text(" " ,modifier = Modifier.padding(top = 16.dp))

            TextField(
                value = texto2,
                onValueChange = { texto2 = it }, // actualiza texto1 cuando escribes
                label = { Text("segundo número") },
                placeholder = { Text("Escribe el segundo número")}, // texto gris de ayuda
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button (
                onClick = {
                    resultado = "" + (num1+num2)
                    resultadoTxt = "$texto1 + $texto2 = $resultado"
                }
            ){
                Text("+")
            }
            Spacer(Modifier.width(16.dp))
            Button (
                onClick = {
                    resultado = "" + (num1-num2)
                    resultadoTxt = "$texto1 - $texto2 = $resultado"
                }
            ){
                Text("-")
            }

        }
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center  //Centrar los botones
        ){
            Button (
                onClick = {
                    resultado = "" + (num1*num2)
                    resultadoTxt = "$texto1 x $texto2 = $resultado"
                }
            ){
                Text("x")
            }
            Spacer(Modifier.width(16.dp))
            Button (
                onClick = {
                    if(num2 != 0) {
                        resultado = "" + (num1 / num2)
                        resultadoTxt = "$texto1 / $texto2 = $resultado"
                    }
                    else{
                        resultadoTxt = "No se puede dividir entre 0 "
                    }
                }
            ){
                Text("/")
            }
        }

        Row (modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.Center
        ){
            Button (
                onClick = {
                    if(num2 != 0){
                        resultado = "" + (num1 % num2)
                        resultadoTxt = "$texto1 % $texto2 = $resultado"
                    }else{
                        resultadoTxt = "No se puede % 0"
                    }
                }
            ){
                Text("%")
            }
            Spacer(Modifier.width(16.dp))

            // limiar todo
            Button (
                onClick = {
                    texto1 = ""
                    texto2 = ""
                    resultado = ""
                    resultadoTxt = ""
                }
            ){
                Text("C")
            }
        }

        //Text(resultadoTxt)
        Text(
            text = resultadoTxt,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.LightGray)  // fondo gris claro
                .padding(12.dp),              // espacio dentro del fondo
            color = if (resultadoTxt.contains("No se puede")) Color.Red else Color.Black, //  rojo si error
            fontWeight = FontWeight.Bold,     // resultado en negrita
        )
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWorldTheme {
        Calculadora()
    }
}