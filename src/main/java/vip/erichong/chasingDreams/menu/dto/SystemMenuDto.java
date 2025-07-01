package vip.erichong.chasingDreams.menu.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eric
 */
@Data
public class SystemMenuDto implements Serializable {
	private Long id;
	private String name;
	private String title;
	private String icon;
	private String path;
	private String component;
	private Integer type;
	private List<SystemMenuDto> children = new ArrayList<>();

}
