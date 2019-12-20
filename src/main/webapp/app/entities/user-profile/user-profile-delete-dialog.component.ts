import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { UserProfileService } from './user-profile.service';

@Component({
  templateUrl: './user-profile-delete-dialog.component.html'
})
export class UserProfileDeleteDialogComponent {
  userProfile: IUserProfile;

  constructor(
    protected userProfileService: UserProfileService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.userProfileService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'userProfileListModification',
        content: 'Deleted an userProfile'
      });
      this.activeModal.dismiss(true);
    });
  }
}
