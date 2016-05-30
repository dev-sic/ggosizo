package com.kosta.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosta.domain.BoardVO;
import com.kosta.domain.Criteria;
import com.kosta.domain.PageMaker;
import com.kosta.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Inject
	private BoardService service;
	
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerGET()throws Exception{
		logger.info("register GET요청...");
		return "board/register"; //스프링 컨테이너에게 뷰 정보를 전달.
	}
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPOST(BoardVO board,RedirectAttributes rttr)throws Exception{
		logger.info("register POST요청...");
		logger.info("BoardVO::"+board);
		service.regist(board);
		
		rttr.addFlashAttribute("msg","SUCCESS");
		
		
		//return "board/success"; //스프링 컨테이너에게 뷰 정보를 전달.
		return "redirect:/board/listAll"; //redirect를 사용하므로 등록글 재입력을 방지한다.
	}	
	
	@RequestMapping("/listAll")
	public String listAll(Model model)throws Exception{
		logger.info("전체list 요청..."+model);
		model.addAttribute("list",service.listAll());
		return "board/listAll";
	}
	
	@RequestMapping("/listCri") //게시물 특정페이지 목록 요청
	public String listAll2(Model model,Criteria cri)throws Exception{
		logger.info("특정페이지 list 요청..."+model);
		model.addAttribute("list",service.listCriteria(cri));
		return "board/listCri";
	}
	
	@RequestMapping(value="/listPage",method=RequestMethod.GET) //게시물 특정페이지 목록 요청
	public String listPage(Model model,Criteria cri)throws Exception{
		logger.info("특정페이지 list 요청..."+model);
		model.addAttribute("list",service.listCriteria(cri));
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.listCountCriteria());
		
		model.addAttribute("pageMaker",pageMaker);
		return "board/listPage";
	}
	
	@RequestMapping("/read")
	public String read(int bno,Model model)throws Exception{
		logger.info("게시물읽기...["+bno+"]");
		model.addAttribute("boardVO",service.read(bno));
		return "board/read";
	}
	
	@RequestMapping("/readPage")
	public String readPage(int bno,@ModelAttribute("cri") Criteria cri,Model model)throws Exception{
		logger.info("게시물읽기...["+bno+"],페이지: "+cri.getPage());
		model.addAttribute("boardVO",service.read(bno));
		return "board/readPage";
	}
	
	@RequestMapping("/remove")
	public String remove(@RequestParam("bno")int board,RedirectAttributes rttr)throws Exception{
		logger.info("게시물 삭제...["+board+"]");
		service.remove(board); 
		
		rttr.addFlashAttribute("msg","SUCCESS");
		return "redirect:/board/listAll";
	}
	
	@RequestMapping(value="/removePage",method=RequestMethod.POST)
	public String removePage(int bno,Criteria cri,RedirectAttributes rttr)throws Exception{
		logger.info("게시물 삭제...["+bno+"]");
		service.remove(bno); 
		
		rttr.addFlashAttribute("page",cri.getPage());
		rttr.addFlashAttribute("perPageNum",cri.getPerPageNum());
		rttr.addFlashAttribute("msg","SUCCESS");
		return "redirect:/board/listPage";
	}
	
	@RequestMapping(value="/modify",method= RequestMethod.GET)
	public String modifyGET(BoardVO board,Model model)throws Exception{ //수정폼 보이기
		logger.info("modify GET요청...");
		logger.info("bno="+board.getBno());
		model.addAttribute(board);
		
		return "board/modify";
	}
	
	@RequestMapping(value="/modifyPage",method= RequestMethod.GET)
	public String modifyPage(BoardVO board,@ModelAttribute("cri") Criteria cri,Model model)throws Exception{ //수정폼 보이기
		logger.info("modify GET요청...");
		logger.info("bno="+board.getBno());
		model.addAttribute(board);
		
		return "board/modify";
	}
	
	@RequestMapping(value="/modify",method= RequestMethod.POST)
	public String modifyPOST(BoardVO board,RedirectAttributes rttr)throws Exception{ //수정 처리
		logger.info("modify POST요청...");
		logger.info("bno="+board.getBno());
		service.modify(board);
		
		rttr.addFlashAttribute("msg","SUCCESS");
		
		return "redirect:/board/listAll";
	}
}
