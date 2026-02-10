package com.example.buscaminas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import com.example.buscaminas.ui.theme.BuscaminasTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class DBHandler(context: Context) :
    SQLiteOpenHelper(context, "buscaminasDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE victorias (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT," +
                    "fecha TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS victorias")
        onCreate(db)
    }

    fun agregarVictoria(nombre: String) {
        val db = this.writableDatabase
        val valores = ContentValues()
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        valores.put("nombre", nombre)
        valores.put("fecha", fecha)
        db.insert("victorias", null, valores)
        db.close()
    }

    fun obtenerVictorias(): List<Pair<String, String>> {
        val lista = mutableListOf<Pair<String, String>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT nombre, fecha FROM victorias ORDER BY id DESC", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Pair(cursor.getString(0), cursor.getString(1)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}

class Buscaminas : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = DBHandler(this)

        setContent {
            BuscaminasTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "inicio") {

                    composable("inicio") {
                        PantallaInicio { nombre, filas, columnas, minas ->
                            navController.navigate("juego/$nombre/$filas/$columnas/$minas")
                        }
                    }

                    composable("juego/{nombre}/{filas}/{columnas}/{minas}") { backStack ->
                        val nombre = backStack.arguments?.getString("nombre") ?: ""
                        val filas = backStack.arguments?.getString("filas")?.toInt() ?: 0
                        val columnas = backStack.arguments?.getString("columnas")?.toInt() ?: 0
                        val minas = backStack.arguments?.getString("minas")?.toInt() ?: 0

                        BuscaminasJuego(
                            filas = filas,
                            columnas = columnas,
                            numMinas = minas,
                            nombreJugador = nombre,
                            db = db,
                            onFinJuego = { resultado ->
                                navController.navigate("fin/$resultado/$nombre")
                            }
                        )
                    }

                    composable("fin/{resultado}/{nombre}") { backStack ->
                        val resultado = backStack.arguments?.getString("resultado") ?: ""
                        val nombre = backStack.arguments?.getString("nombre") ?: ""
                        PantallaFin(resultado, nombre, db) {
                            navController.navigate("inicio") {
                                popUpTo("inicio") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(onEmpezar: (String, Int, Int, Int) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var seleccion by remember { mutableStateOf("Selecciona tamaño") }

    val opciones = listOf("3x3 (3 minas)", "5x5 (5 minas)", "10x10 (10 minas)")

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(" Buscaminas ", fontSize = 32.sp)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del jugador") }
        )

        Spacer(Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = seleccion,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            seleccion = opcion
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val (f, c, m) = when (seleccion) {
                    "3x3 (3 minas)" -> Triple(3, 3, 3)
                    "5x5 (5 minas)" -> Triple(5, 5, 5)
                    "10x10 (10 minas)" -> Triple(10, 10, 10)
                    else -> Triple(0, 0, 0)
                }
                if (nombre.isNotBlank() && f > 0)
                    onEmpezar(nombre, f, c, m)
            },
            enabled = nombre.isNotBlank() && seleccion != "Selecciona tamaño"
        ) {
            Text("Empezar juego")
        }
    }
}


fun generarMinas(total: Int, cantidad: Int): BooleanArray {
    val arr = BooleanArray(total)
    var minasSalidas = 0
    while (minasSalidas < cantidad) {
        val i = Random.nextInt(total)
        if (!arr[i]) {
            arr[i] = true
            minasSalidas++
        }
    }
    return arr
}

fun hayMina(index: Int, minas: BooleanArray): Boolean = minas.getOrNull(index) == true

fun contarMinasAlrededor(index: Int, filas: Int, columnas: Int, minas: BooleanArray): Int {
    val fila = index / columnas
    val col = index % columnas
    var contador = 0
    for (df in -1..1) {
        for (dc in -1..1) {
            if (df == 0 && dc == 0) continue
            val nf = fila + df
            val nc = col + dc
            if (nf in 0 until filas && nc in 0 until columnas) {
                val vecinoIndex = nf * columnas + nc
                if (minas[vecinoIndex]) contador++
            }
        }
    }
    return contador
}

fun desvelarCasilla(
    index: Int,
    filas: Int,
    columnas: Int,
    minas: BooleanArray,
    reveladas: List<Boolean>
): List<Boolean> {
    var nuevo = reveladas.toMutableList()
    val fila = index / columnas
    val col = index % columnas

    if (nuevo[index] || minas[index]) return nuevo
    nuevo[index] = true

    val minasCerca = contarMinasAlrededor(index, filas, columnas, minas)
    if (minasCerca > 0) return nuevo

    for (df in -1..1) {
        for (dc in -1..1) {
            if (df == 0 && dc == 0) continue
            val nf = fila + df
            val nc = col + dc
            if (nf in 0 until filas && nc in 0 until columnas) {
                val vecinoIndex = nf * columnas + nc
                if (!minas[vecinoIndex] && !nuevo[vecinoIndex]) {
                    nuevo = desvelarCasilla(vecinoIndex, filas, columnas, minas, nuevo).toMutableList()
                }
            }
        }
    }
    return nuevo
}

@Composable
fun BuscaminasJuego(
    filas: Int,
    columnas: Int,
    numMinas: Int,
    nombreJugador: String,
    db: DBHandler,
    onFinJuego: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val total = filas * columnas
    var minas by remember { mutableStateOf(generarMinas(total, numMinas)) }
    var reveladas by remember { mutableStateOf(List(total) { false }) }
    var gameOver by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // CABECERA
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Blue),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Jugador: $nombreJugador",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Tablero: ${filas}x${columnas} | Minas: $numMinas",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                }
                if (mensaje.isNotEmpty()) {
                    Text(
                        text = if (mensaje.contains("mina")) "💥" else "🏆",
                        fontSize = 22.sp
                    )
                }
            }
        }

        // MENSAJE DE ESTADO
        if (mensaje.isNotEmpty()) {
            Text(
                mensaje,
                color = if (mensaje.contains("mina")) Color.Red else Color.Green,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }

        // TABLERO
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnas),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(total) { index ->
                val revelada = reveladas[index]
                val esMina = hayMina(index, minas)
                val minasCerca = contarMinasAlrededor(index, filas, columnas, minas)
                val colorCasilla = when {
                    revelada && esMina -> Color.Red
                    revelada -> Color.LightGray
                    else -> (Color.Blue)
                }

                Button(
                    onClick = {
                        if (!reveladas[index] && !gameOver) {
                            if (esMina) {
                                gameOver = true
                                mensaje = "¡Has pisado una mina!"
                                reveladas = List(total) { i -> reveladas[i] || minas[i] }
                                scope.launch {
                                    delay(2000)
                                    onFinJuego("perdiste")
                                }
                            } else {
                                reveladas = desvelarCasilla(index, filas, columnas, minas, reveladas)
                                val seguras = total - numMinas
                                if (reveladas.count { it } >= seguras) {
                                    gameOver = true
                                    mensaje = "¡Has ganado!"
                                    db.agregarVictoria(nombreJugador)
                                    onFinJuego("ganaste")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(1.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorCasilla),
                    shape = RectangleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    if (revelada) {
                        when {
                            esMina -> Text("💣", fontSize = 12.sp)
                            minasCerca > 0 -> {
                                val fontSize = when {
                                    filas >= 10 -> 10.sp
                                    filas >= 5 -> 14.sp
                                    else -> 18.sp
                                }
                                Text(minasCerca.toString(), fontSize = fontSize, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun PantallaFin(resultado: String, nombre: String, db: DBHandler, onVolver: () -> Unit) {
    val victorias = remember { db.obtenerVictorias() }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (resultado == "ganaste") " ¡Has ganado! " else " Has perdido ",
            color = if (resultado == "ganaste") Color.Green else Color.Red,
            fontSize = 26.sp
        )

        Spacer(Modifier.height(24.dp))
        Text(" Historial de victorias: ", fontSize = 20.sp)
        victorias.forEach {
            Text("• ${it.first} — ${it.second}")
        }

        Spacer(Modifier.height(32.dp))
        Button(onClick = onVolver) { Text("Volver al inicio") }
    }
}

@Preview(showBackground = true)
@Composable
fun BuscaminasPreview() {
    val context = LocalContext.current
    BuscaminasTheme {
        BuscaminasJuego(
            filas = 5,
            columnas = 5,
            numMinas = 5,
            nombreJugador = "Test",
            db = DBHandler(context),
            onFinJuego = {}
        )
    }
}

