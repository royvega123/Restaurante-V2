package postres2

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import java.util.*
import java.util.concurrent.CompletableFuture.supplyAsync

class Postres2Controller: CrudHandler{
    override fun create(ctx: Context) {
        ctx.result(supplyAsync{ Postres2Service.insert(ctx) })
    }

    override fun delete(ctx: Context, resourceId: String) {
        println(resourceId)
        ctx.result(supplyAsync { Postres2Service.DeleteById(ctx, resourceId)} )
    }

    override fun getAll(ctx: Context) {
        ctx.json(supplyAsync { Postres2Service.selectedAll(ctx)})
    }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.json(supplyAsync { Postres2Service.selectById(resourceId) })
    }

    override fun update(ctx: Context, resourceId: String) {
        println(resourceId)
        ctx.result(supplyAsync { Postres2Service.UpdateById( ctx, resourceId ) })
    }

}