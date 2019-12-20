import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRateXProfile } from 'app/shared/model/rate-x-profile.model';
import { RateXProfileService } from './rate-x-profile.service';

@Component({
  templateUrl: './rate-x-profile-delete-dialog.component.html'
})
export class RateXProfileDeleteDialogComponent {
  rateXProfile: IRateXProfile;

  constructor(
    protected rateXProfileService: RateXProfileService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.rateXProfileService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'rateXProfileListModification',
        content: 'Deleted an rateXProfile'
      });
      this.activeModal.dismiss(true);
    });
  }
}
