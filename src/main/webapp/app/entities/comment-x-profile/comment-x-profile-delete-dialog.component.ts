import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICommentXProfile } from 'app/shared/model/comment-x-profile.model';
import { CommentXProfileService } from './comment-x-profile.service';

@Component({
  templateUrl: './comment-x-profile-delete-dialog.component.html'
})
export class CommentXProfileDeleteDialogComponent {
  commentXProfile: ICommentXProfile;

  constructor(
    protected commentXProfileService: CommentXProfileService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.commentXProfileService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'commentXProfileListModification',
        content: 'Deleted an commentXProfile'
      });
      this.activeModal.dismiss(true);
    });
  }
}
