package binar.box.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binar.box.domain.Lock;
import binar.box.domain.Panel;
import binar.box.domain.User;
import binar.box.dto.LockResponseDTO;
import binar.box.dto.PanelDTO;
import binar.box.repository.ConfigurationRepository;
import binar.box.repository.LockRepository;
import binar.box.repository.PanelRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 11-Jun-18.
 */
@Service
public class PanelService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LockRepository lockRepository;
	@Autowired
	private PanelRepository panelRepository;
	@Autowired
	private LockService lockService;
	@Autowired
	private UserService userService;
	@Autowired
	private ConfigurationRepository configurationRepository;

	public List<PanelDTO> getAllPanels() {
		var user = userService.getAuthenticatedUser();
		var panels = panelRepository.findAllPanelsBasedOnLocation(user.getCountry());
		panels = panels.parallelStream().map(this::insertRandomLocks).collect(Collectors.toList());
		panels = panels.parallelStream().map(this::addPanelLocks).collect(Collectors.toList());
		var maxLights = configurationRepository.findById(Long.valueOf(1)).get().getGlitteringLights();
		var maxPanels = panels.size();
		addLockLights(panels, maxLights, maxPanels);
		return panels.parallelStream().map(this::toPanelDto).collect(Collectors.toList());
	}

	private void addLockLights(List<Panel> panels, int maxLights, int maxPanels) {
		for (Panel panel : panels) {
			var lightsPerPanel = maxLights / maxPanels;
			var locks = panel.getLocks();
			setLockLight(lightsPerPanel, locks);
		}
	}

	private void setLockLight(int lightsPerPanel, List<Lock> locks) {
		var lockIndex = 0;
		for (var index = 1; index <= lightsPerPanel; index++) {
			while (lockIndex < locks.size()) {
				var lock = locks.get(lockIndex);
				lock.setGlitteringLight(true);
				lockIndex++;
				break;
			}
		}
	}

	private PanelDTO toPanelDto(Panel panel) {
		var panelDTO = new PanelDTO();
		panelDTO.setId(panel.getId());
		var lockResponseDTOList = panel.getLocks().parallelStream().map(this::toLockResponse)
				.collect(Collectors.toList());
		panelDTO.setLockResponseDTO(lockResponseDTOList);
		return panelDTO;
	}

	private LockResponseDTO toLockResponse(Lock lock) {
		return lockService.toLockResponseDto(lock);
	}

	Panel getPanel(long panelId) {
		return panelRepository.findById(panelId).orElseThrow(() -> new LockBridgesException(Constants.PANEL_NOT_FOUND));
	}

	public PanelDTO getPanelDTO(long id) {
		return toPanelDto(getPanel(id));
	}

	void maintainPanels() {
		log.info(Constants.MAINTAINING_PANELS);
		var numberOfPanelsAvailable = panelRepository.countPanels()
				.orElseThrow(() -> new LockBridgesException(Constants.SOMETHING_WENT_WRONG_WITH_PANELS_COUNTING));
		if (numberOfPanelsAvailable.intValue() < 2) {
			panelRepository.addPanels(2);
		} else if (numberOfPanelsAvailable.intValue() <= 2) {
			panelRepository.addPanels(1);
		}
	}

	public List<PanelDTO> getUserLocksAndPanels() {
		var user = userService.getAuthenticatedUser();
		var panelsOfUser = getPanelsWhereUserHasLocks(user);
		if (user.isLinkedWithFacebbok()) {
			var facebookUserFriends = userService.getUserFacebookFriends(user);
			panelsOfUser = panelsOfUser.parallelStream().map(panel -> addPanelLocks(panel, user, facebookUserFriends))
					.collect(Collectors.toList());

		} else {
			panelsOfUser = panelsOfUser.parallelStream().map(this::addPanelLocks).collect(Collectors.toList());
		}
		panelsOfUser = panelsOfUser.parallelStream().map(this::insertRandomLocks).collect(Collectors.toList());
		return panelsOfUser.parallelStream().map(this::toPanelDto).collect(Collectors.toList());
	}

	private Panel addPanelLocks(Panel panel) {
		if (panel.getLocks() == null) {
			panel.setLocks(lockRepository.findByPanelId(panel.getId()));
		} else {
			panel.getLocks().addAll(lockRepository.findByPanelId(panel.getId()));
		}
		return panel;
	}

	private Panel addPanelLocks(Panel panel, User user, List<String> facebookUserFriends) {
		panel.setLocks(lockRepository.findUserPanelLocksAndHidePrivateFriendsLocks(user.getId(), panel.getId(),
				facebookUserFriends));
		return panel;
	}

	private Panel insertRandomLocks(Panel panel) {
		var panelMaxSize = configurationRepository.findById(Long.valueOf(1)).get().getPanelMaxSize();
		var numberOfRandomLocksOnUserPanel = configurationRepository.findById(Long.valueOf(1)).get()
				.getRandomLocksOnUserPanel();
		List<Lock> locks = panel.getLocks() == null ? new ArrayList<>() : panel.getLocks();
		for (var times = 0; times < numberOfRandomLocksOnUserPanel; times++) {
			if (locks.size() >= panelMaxSize) {
				break;
			}
			var lock = new Lock();
			lock.setId(Long.valueOf(times + 1000));
			locks.add(lock);
			panel.setLocks(locks);
		}
		return panel;
	}

	private List<Panel> getPanelsWhereUserHasLocks(User user) {
		return panelRepository.findByUser(user.getId());
	}
}
