package com.pet.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.pet.controller.Action;
import com.pet.controller.ActionForward;
import com.pet.model.ReviewDAO;
import com.pet.model.ReviewDTO;

public class UpdateOkReviewAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 수정완료 된 비지니스 로직

		ReviewDTO dto = new  ReviewDTO();

		// 첨부파일이 저장될 경로
		String saveFolder = "D:\\ncs\\workspace(jsp)\\PetProject\\WebContent\\review";

		// 첨부파일 최대 크기 지정
		int fileSize = 10 * 1024 * 1024; // 10 * 1Mb(1024*1024) => 10MB

		// 파일 업로드 진행 시 이진 파일 업로드를 위한 객체 생성
		MultipartRequest multi = new MultipartRequest(request, // 일반적인 request 객체
				saveFolder, // 첨부파일이 저장될 경로
				fileSize, // 업로드할 파일의 최대 크기
				"UTF-8", // 문자인코딩 방식
				new DefaultFileRenamePolicy()); // 파일의 이름이 같은 경우 중목이 안되게 설정 ex) 새파일 새파일(1) 새파일(2)

		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		
		int pnum = Integer.parseInt(multi.getParameter("pnum").trim());
		int SaleNo = Integer.parseInt(multi.getParameter("SaleNo").trim());
		String cont = multi.getParameter("cont").trim();
		String old_image = multi.getParameter("old_image").trim();

		// 자료실 폼 페이지에서 type="file"로 되어있으면 getFile() 메서드로 받기
		File upload_file = multi.getFile("new_image");

		if (upload_file != null) { // 첨부파일이 있다면

			// 01. 우선은 첨부 파일의 이름을 알아야함
			// getName() : 첨부파일의 이름을 문자열로 반환해주는 메서드
			String fielName = upload_file.getName();

			// 02. 날짜 객체 생성
			Calendar cal = Calendar.getInstance();

			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);

			// ...(파일주소)..../upload/2022-04-18
			String homedir = saveFolder + "/" + year + "-" + month + "-" + day;

			// 02-1. 날짜 폴더를 만들어보자
			File path1 = new File(homedir);

			if (!path1.exists()) { // 폴더가 존재하지 않는 경우
				path1.mkdir(); // 실제 폴더를 만드는 메서드
			}

			// 파일을 만들어보자 => ex) 홍길동(작성자)_파일명
			// ...(파일주소)..../upload/2022-04-18/홍길동_파일명
			String reFileName = userId + "_" + fielName;
			upload_file.renameTo(new File(homedir + "/" + reFileName)); // 파일 이름을 바꿔주는 메서드

			// 실제 DB에 저장되는 파일 이름 => "/2022-04-18/홍길동_파일명"
			String fileDBName = "/" + year + "-" + month + "-" + day + "/" + reFileName;

			dto.setRimage(fileDBName);

		}else {
			dto.setRimage(old_image);
		}
		
		dto.setPnum(pnum); dto.setSale_no(SaleNo);
		dto.setReview_cont(cont); dto.setUserID(userId);
		
		ReviewDAO dao = ReviewDAO.getInstance();
		
		int check = dao.updateReview(dto);
		
		ActionForward forward = new ActionForward();
		
		
		if(check>0) {
			
			forward.setRedirect(true);
			forward.setPath("selectMyReview.do");
		}else {
			PrintWriter out = response.getWriter();
			
			out.println("<script>");
			out.println("alert('게시글 추가 실패')");
			out.println("history.back()");
			out.println("</script>");
		}


		return forward;
	}

}
