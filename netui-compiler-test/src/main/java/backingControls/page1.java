package backingControls;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.FacesBackingBean;


@Jpf.FacesBacking
public class page1
		extends FacesBackingBean {
	@org.apache.beehive.controls.api.bean.Control()
	private TestControl ctrl;

	private String _message = "";

	public void setMessage(String message) {
		_message = message;
	}

	public String getMessage() {
		return _message;
	}

	public String doit() {
		_message = ctrl.sayHello();
		return null;
	}
}
