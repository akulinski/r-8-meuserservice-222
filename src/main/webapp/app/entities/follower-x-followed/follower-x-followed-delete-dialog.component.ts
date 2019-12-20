import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFollowerXFollowed } from 'app/shared/model/follower-x-followed.model';
import { FollowerXFollowedService } from './follower-x-followed.service';

@Component({
  templateUrl: './follower-x-followed-delete-dialog.component.html'
})
export class FollowerXFollowedDeleteDialogComponent {
  followerXFollowed: IFollowerXFollowed;

  constructor(
    protected followerXFollowedService: FollowerXFollowedService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.followerXFollowedService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'followerXFollowedListModification',
        content: 'Deleted an followerXFollowed'
      });
      this.activeModal.dismiss(true);
    });
  }
}
