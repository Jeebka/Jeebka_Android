package edu.escuelaing.ieti.jeebka.GroupDetailsView;

import java.util.ArrayList;

import edu.escuelaing.ieti.jeebka.GroupsView.DynamicGroupRvModel;

public interface UpdateLinkRecyclerView {
    public void callBack(int position, ArrayList<DynamicLinksRvModel> items);
    public void callBack(int position, ArrayList<DynamicLinksRvModel> items,DynamicLinksRvModel toEliminate);
}
