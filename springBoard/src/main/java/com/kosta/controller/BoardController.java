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
		logger.info("register GET��û...");
		return "board/register"; //������ �����̳ʿ��� �� ������ ����.
	}
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPOST(BoardVO board,RedirectAttributes rttr)throws Exception{
		logger.info("register POST��û...");
		logger.info("BoardVO::"+board);
		service.regist(board);
		
		rttr.addFlashAttribute("msg","SUCCESS");
		
		
		//return "board/success"; //������ �����̳ʿ��� �� ������ ����.
		return "redirect:/board/listAll"; //redirect�� ����ϹǷ� ��ϱ� ���Է��� �����Ѵ�.
	}	
	
	@RequestMapping("/listAll")
	public String listAll(Model model)throws Exception{
		logger.info("��ülist ��û..."+model);
		model.addAttribute("list",service.listAll());
		return "board/listAll";
	}
	
	@RequestMapping("/listCri") //�Խù� Ư�������� ��� ��û
	public String listAll2(Model model,Criteria cri)throws Exception{
		logger.info("Ư�������� list ��û..."+model);
		model.addAttribute("list",service.listCriteria(cri));
		return "board/listCri";
	}
	
	@RequestMapping(value="/listPage",method=RequestMethod.GET) //�Խù� Ư�������� ��� ��û
	public String listPage(Model model,Criteria cri)throws Exception{
		logger.info("Ư�������� list ��û..."+model);
		model.addAttribute("list",service.listCriteria(cri));
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.listCountCriteria());
		
		model.addAttribute("pageMaker",pageMaker);
		return "board/listPage";
	}
	
	@RequestMapping("/read")
	public String read(int bno,Model model)throws Exception{
		logger.info("�Խù��б�...["+bno+"]");
		model.addAttribute("boardVO",service.read(bno));
		return "board/read";
	}
	
	@RequestMapping("/readPage")
	public String readPage(int bno,@ModelAttribute("cri") Criteria cri,Model model)throws Exception{
		logger.info("�Խù��б�...["+bno+"],������: "+cri.getPage());
		model.addAttribute("boardVO",service.read(bno));
		return "board/readPage";
	}
	
	@RequestMapping("/remove")
	public String remove(@RequestParam("bno")int board,RedirectAttributes rttr)throws Exception{
		logger.info("�Խù� ����...["+board+"]");
		service.remove(board); 
		
		rttr.addFlashAttribute("msg","SUCCESS");
		return "redirect:/board/listAll";
	}
	
	@RequestMapping(value="/removePage",method=RequestMethod.POST)
	public String removePage(int bno,Criteria cri,RedirectAttributes rttr)throws Exception{
		logger.info("�Խù� ����...["+bno+"]");
		service.remove(bno); 
		
		rttr.addFlashAttribute("page",cri.getPage());
		rttr.addFlashAttribute("perPageNum",cri.getPerPageNum());
		rttr.addFlashAttribute("msg","SUCCESS");
		return "redirect:/board/listPage";
	}
	
	@RequestMapping(value="/modify",method= RequestMethod.GET)
	public String modifyGET(BoardVO board,Model model)throws Exception{ //������ ���̱�
		logger.info("modify GET��û...");
		logger.info("bno="+board.getBno());
		model.addAttribute(board);
		
		return "board/modify";
	}
	
	@RequestMapping(value="/modifyPage",method= RequestMethod.GET)
	public String modifyPage(BoardVO board,@ModelAttribute("cri") Criteria cri,Model model)throws Exception{ //������ ���̱�
		logger.info("modify GET��û...");
		logger.info("bno="+board.getBno());
		model.addAttribute(board);
		
		return "board/modify";
	}
	
	@RequestMapping(value="/modify",method= RequestMethod.POST)
	public String modifyPOST(BoardVO board,RedirectAttributes rttr)throws Exception{ //���� ó��
		logger.info("modify POST��û...");
		logger.info("bno="+board.getBno());
		service.modify(board);
		
		rttr.addFlashAttribute("msg","SUCCESS");
		
		return "redirect:/board/listAll";
	}
}
