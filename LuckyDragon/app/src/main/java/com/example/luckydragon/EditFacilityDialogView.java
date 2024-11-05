package com.example.luckydragon;

public class EditFacilityDialogView extends Observer {
    private final EditFacilityDialogFragment editFacilityDialogFragment;

    public EditFacilityDialogView(User user, EditFacilityDialogFragment editFacilityDialogFragment) {
        this.editFacilityDialogFragment = editFacilityDialogFragment;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        // This is a simple dialog fragment so not sure any update action is neccessary
    }
}
