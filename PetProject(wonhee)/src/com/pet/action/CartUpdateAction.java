package com.pet.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pet.controller.Action;
import com.pet.controller.ActionForward;
import com.pet.model.CartDAO;
import com.pet.model.CartDTO;

public class CartUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		

		int cart_pqty = Integer.parseInt(request.getParameter("pqty").trim());
		
		int cart_no = Integer.parseInt(request.getParameter("no").trim());
		
		CartDAO dao = CartDAO.getInstance();
		
		int check = dao.updateCart(cart_pqty, cart_no);
		
		ActionForward forward = new ActionForward();
		
		PrintWriter out = response.getWriter();
		
		if(check>0) {
			forward.setRedirect(true);
			forward.setPath("cart_list.do");
		}else {
			out.println("<script>");
			out.println("alert('장바구니 수량 수정 실패')");
			out.println("history.back()");
			out.println("</script>");
		}
		
		return forward;
	}

}
