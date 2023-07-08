package pl.sowacustoms.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

@ControllerAdvice
public class SinglePageController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleStaticResourceNotFound(final NoHandlerFoundException e, HttpServletRequest request,
                                               RedirectAttributes redirectAttributes) {
        if(request.getRequestURI().startsWith("/api"))
            return this.getApiResourceNotFoundBody(e, request);
        else {
            try {
                File indexFile = ResourceUtils.getFile("classpath:public/index.html");
                FileInputStream inputStream = new FileInputStream(indexFile);
                String body = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
                return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(body);
            } catch (IOException ex) {
                ex.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error completing the action.");
            }
        }
    }

    private ResponseEntity<String> getApiResourceNotFoundBody(NoHandlerFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>("Not Found !!", HttpStatus.NOT_FOUND);
    }
}
