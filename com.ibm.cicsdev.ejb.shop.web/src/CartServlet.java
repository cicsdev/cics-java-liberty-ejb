
import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.cicsdev.ejb.CartBean;

@WebServlet("/cart")
public class CartServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        
        if(session.isNew())
        {
            CartBean cart;
            try
            {
                cart = InitialContext.doLookup("");
                session.setAttribute("cart", cart);
            }
            catch (NamingException e)
            {
                // ...
            }
            
        }
        
        CartBean cart = (CartBean) session.getAttribute("cart");
        
        // ...
    }
}
