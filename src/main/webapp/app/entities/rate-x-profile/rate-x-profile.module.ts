import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { R8Meuserservice2SharedModule } from 'app/shared/shared.module';
import { RateXProfileComponent } from './rate-x-profile.component';
import { RateXProfileDetailComponent } from './rate-x-profile-detail.component';
import { RateXProfileUpdateComponent } from './rate-x-profile-update.component';
import { RateXProfileDeleteDialogComponent } from './rate-x-profile-delete-dialog.component';
import { rateXProfileRoute } from './rate-x-profile.route';

@NgModule({
  imports: [R8Meuserservice2SharedModule, RouterModule.forChild(rateXProfileRoute)],
  declarations: [RateXProfileComponent, RateXProfileDetailComponent, RateXProfileUpdateComponent, RateXProfileDeleteDialogComponent],
  entryComponents: [RateXProfileDeleteDialogComponent]
})
export class R8Meuserservice2RateXProfileModule {}
