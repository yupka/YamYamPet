package com.admin.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.admin.controller.Action;
import com.admin.controller.ActionForward;
import com.pet.model.ProductDAO;
import com.pet.model.ProductDTO;

public class AdminProductListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 상품 등록 폼 페이지를 통해서 DB에 저장된 전체 상품 목록을 조회하여
		// 상품 목록 페이지(view page)로 이동시키는 비지니스 로직.
		
		// 페이징 처리 작업 진행
		int rowsize = 5;		// 한 페이지 당 보여질 게시물의 수
		int block = 5;			// 아래에 보여질 페이지의 최대 수 - 예)[1][2][3] / [3][4][5]
		int totalRecord = 0;	// DB상의 게시물의 전체 수
		int allPage = 0;		// 전체 페이지 수
		
		int page = 0;			// 현재 페이지 변수
		
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page").trim());
		} else {	// 처음으로 [전체 게시물 목록] a 태그를 선택한 경우
			page = 1;
		}
		
		// 해당 페이지에서 시작 번호
		int startNo = (page * rowsize) - (rowsize - 1);
		
		// 해당 페이지에서 끝 번호
		int endNo = (page * rowsize);
		
		// 해당 페이지에서 시작 블럭
		int startBlock = (((page - 1) / block) * block) + 1;
		
		// 해당 페이지에서 끝 블럭
		int endBlock = (((page - 1) / block) * block) + block;
		
		ProductDAO dao = ProductDAO.getInstance();
		
		// DB상의 전체 게시물 수를 확인하는 메서드 호출
		totalRecord = dao.getProductCount();
		
		// 전체 게시물의 수를 한 페이지당 보여질 게시물의 수로 나누어 주어야 함.
		// 이 과정을 거치면 전체 페이지 수가 나오게 됨.
		// 전체 페이지 수가 나올 때 나머지가 있으면 무조건 페이지 수를 하나 올려주어야 함.
		allPage = (int)Math.ceil(totalRecord / (double)rowsize);
		
		if(endBlock > allPage) {
			endBlock = allPage;
		}
		
		// 현재 페이지에서 해당하는 게시물을 가져오는 메서드 호출
		List<ProductDTO> list = dao.getProductList(page, rowsize);
		
		// 지금까지 페이징 처리 시 작업했던 모든 값들을 view page로 이동시키자.
		request.setAttribute("page", page);
		request.setAttribute("rowsize", rowsize);
		request.setAttribute("block", block);
		request.setAttribute("totalRecord", totalRecord);
		request.setAttribute("allPage", allPage);
		request.setAttribute("startNo", startNo);
		request.setAttribute("endNo", endNo);
		request.setAttribute("startBlock", startBlock);
		request.setAttribute("endBlock", endBlock);
		request.setAttribute("productList", list);
		
		ActionForward forward = new ActionForward();
		
		forward.setRedirect(false);
		forward.setPath("admin1/admin_product_list.jsp");
		
		return forward;
	}

}
