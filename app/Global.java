import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.mvc.Action;
import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;


public class Global extends GlobalSettings {
//    private class ActionWrapper extends Action.Simple {
//        public ActionWrapper(Action<?> action) {
//            this.delegate = action;
//        }
//
//        @Override
//        public Promise<Result> call(Http.Context ctx) throws java.lang.Throwable {
//            Promise<Result> result = this.delegate.call(ctx);
//            Http.Response response = ctx.response();
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            return result;
//        }
//    }
//    @Override
//    public Action<?> onRequest(Http.Request request, java.lang.reflect.Method actionMethod) {
//        return new ActionWrapper(super.onRequest(request, actionMethod));
//    }
}