import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRateXProfile } from 'app/shared/model/rate-x-profile.model';

@Component({
  selector: 'jhi-rate-x-profile-detail',
  templateUrl: './rate-x-profile-detail.component.html'
})
export class RateXProfileDetailComponent implements OnInit {
  rateXProfile: IRateXProfile;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ rateXProfile }) => {
      this.rateXProfile = rateXProfile;
    });
  }

  previousState() {
    window.history.back();
  }
}
