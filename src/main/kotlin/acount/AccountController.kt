import io.javalin.http.Context
import kat.auth.AccountService
import java.util.concurrent.CompletableFuture
import io.javalin.apibuilder.CrudHandler

class AccountController: CrudHandler {
    override fun create(ctx: Context) {
        ctx.json(CompletableFuture.supplyAsync{ AccountService.create(ctx)})
    }

    override fun delete(ctx: Context, resourceId: String) {
        TODO("Not yet implemented")
    }

    override fun getAll(ctx: Context) {
        ctx.json(CompletableFuture.supplyAsync{ AccountService.getALL() })
    }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.json(CompletableFuture.supplyAsync{ AccountService.findById(resourceId) })
    }

    override fun update(ctx: Context, resourceId: String) {
        ctx.json(CompletableFuture.supplyAsync{ AccountService.update(ctx, resourceId) })
    }

}