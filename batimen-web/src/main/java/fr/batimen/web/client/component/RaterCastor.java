package fr.batimen.web.client.component;

import org.apache.wicket.markup.html.panel.Panel;

public class RaterCastor extends Panel {

    private static final long serialVersionUID = 6704123311798304893L;

    private final String containerName = "rater";

    public RaterCastor(String id) {
        super(id);
        // DropDownChoice<Integer> rater = new
        // DropDownChoice<Integer>(containerName, initListRater());
        // this.add(rater);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Component#onInitialize()
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    /*
     * @Override public void renderHead(IHeaderResponse response) {
     * super.renderHead(response);
     * response.render(OnLoadHeaderItem.forScript("$(function () {$('#" +
     * containerName + "').barrating('show');)")); }
     * 
     * private List<Integer> initListRater() { List<Integer> raterValue = new
     * ArrayList<Integer>(); raterValue.add(1); raterValue.add(2);
     * raterValue.add(3); raterValue.add(4); raterValue.add(5); return
     * raterValue; }
     */
}
