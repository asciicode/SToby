package nz.co.logicons.tlp.mobile.stobyapp.domain.data;

/*
 * @author by Allen
 */
public class DataState<T> {
    T data;
    String error;
    boolean loading;
    Class<T> typeParameterClass;

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public boolean isLoading() {
        return loading;
    }

    public DataState(/*Class<T> typeParameterClass, */T data) {
//        this.typeParameterClass = typeParameterClass;
        this.data = data;
    }
//    public DataState(Class<T> typeParameterClass, boolean loading) {
//        this.typeParameterClass = typeParameterClass;
//        this.loading = loading;
//    }

    public Class<T> getTypeParameterClass() {
        return typeParameterClass;
    }

    //    public DataState(String error) {
//        this.error = error;
//    }

//    public DataState(boolean loading) {
//        this.loading = loading;
//    }

//    public DataState<T> success(T data) {
//        return new DataState<T>(data);
//    }
//
//    public DataState<T> error(String error) {
//        return new DataState<T>(error);
//    }
//
//    public DataState<T> loading() {
//        return new DataState<T>(true);
//    }
}
