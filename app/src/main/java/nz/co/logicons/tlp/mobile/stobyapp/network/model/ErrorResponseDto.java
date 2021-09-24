package nz.co.logicons.tlp.mobile.stobyapp.network.model;

/*
 * @author by Allen
 */
public class ErrorResponseDto {

    private int statusCode;

    private String message;

    public ErrorResponseDto()
    {
    }

    public ErrorResponseDto(int statusCode, String message)
    {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }


}
