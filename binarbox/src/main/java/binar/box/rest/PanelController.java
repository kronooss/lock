package binar.box.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import binar.box.dto.PanelDTO;
import binar.box.service.PanelService;
import binar.box.util.Constants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@RestController
@RequestMapping(value = Constants.API)
public class PanelController {

	@Autowired
	private PanelService panelService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get panel", notes = "Get panels", hidden = true)
	@GetMapping(value = Constants.PANEL_ENDPOINT)
	private List<PanelDTO> getPanels() {
		return panelService.getAllPanels();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get panel", notes = "Get panels", hidden = true)
	@GetMapping(value = Constants.PANEL_ENDPOINT + Constants.USER)
	private List<PanelDTO> getUserAndUserFriendLocksAndPanels() {
		return panelService.getUserLocksAndPanels();
	}

}
