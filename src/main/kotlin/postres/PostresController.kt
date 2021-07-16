package postres

import io.javalin.http.Context

//data class Postre(val name: String, val fat: Double, val carbo: Double, val protein: Double)

object PostresController {
        fun getAllPostres(ctx: Context){
            println("Peticion para solicitar todos los postres")
            ctx.result("Ahi te van todos los postres")
        }

        fun createPostre(ctx: Context){
            //val postres = ctx.bodyValidator<Postre>().get()
            println("Se inserto el postre")
            //println("El postre a guardar es: ${postres}")
            ctx.result("Decir si se inserto el postre")
        }
        fun getPostre(ctx: Context){
            println("Peticion para solicitar un postre")
            ctx.result("Ahi te va 1 postre")
        }
        fun updatePostre(ctx: Context){
            println("Petición para actualizar un postre")
            ctx.result("Se actualizo 1 postre")
        }
        fun deletePostre(ctx: Context){
            println("Petición para eliminar un postre")
            ctx.result("Se elimino 1 postre")
        }
}