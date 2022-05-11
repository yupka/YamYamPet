package com.pet.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pet.controller.Action;
import com.pet.controller.ActionForward;
import com.pet.model.PetMemberDAO;
import com.pet.model.PetMemberDTO;

public class UserJoinOkAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//
		String m_id = request.getParameter("user_id").trim();
		String m_pwd = request.getParameter("user_pwd").trim();
		String m_name = request.getParameter("user_name").trim();
		
		String birthyy = request.getParameter("birthyy").trim();
		String birthmm = request.getParameter("birthmm").trim();
		String birthdd = request.getParameter("birthdd").trim();
		String m_birth = birthyy + "/" + birthmm + "/" + birthdd;
		
		String m_email = request.getParameter("user_email").trim();
		
		String user_phone1 = request.getParameter("user_phone1").trim();
		String user_phone2 = request.getParameter("user_phone2").trim();
		String user_phone3 = request.getParameter("user_phone3").trim();
		String m_phone = user_phone1+"-"+user_phone2+"-"+user_phone3;
		
		int user_addr_no = Integer.parseInt(request.getParameter("user_addr_no").trim());
		String user_addr1 = request.getParameter("user_addr1").trim();
		String user_addr2 = request.getParameter("user_addr2").trim();
		String user_addr3 = request.getParameter("user_addr3").trim();
		String m_addr = "(우."+user_addr_no+") "+user_addr1 + " " + user_addr2 + " " + user_addr3; 
		
		PetMemberDTO dto = new PetMemberDTO();
		dto.setM_id(m_id);
		dto.setM_pwd(m_pwd);
		dto.setM_name(m_name);
		dto.setM_birth(m_birth);
		dto.setM_email(m_email);
		dto.setM_phone(m_phone);
		dto.setM_addr(m_addr);
		
		PetMemberDAO dao = PetMemberDAO.getInstance();
		int check = dao.joinPetMember(dto);
		ActionForward forward = new ActionForward();
		
		PrintWriter out = response.getWriter();

		if (check > 0) {
			forward.setRedirect(false);
			forward.setPath("user/login.jsp"); 
		}else {
			out.println("<script>");
			out.println("alert('오류가 발생했습니다!')");
			out.println("history.back()");
			out.println("</script>");
		}
		
		
		return forward;
	}

}
