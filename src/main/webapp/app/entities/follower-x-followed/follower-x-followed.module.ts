import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { R8Meuserservice2SharedModule } from 'app/shared/shared.module';
import { FollowerXFollowedComponent } from './follower-x-followed.component';
import { FollowerXFollowedDetailComponent } from './follower-x-followed-detail.component';
import { FollowerXFollowedUpdateComponent } from './follower-x-followed-update.component';
import { FollowerXFollowedDeleteDialogComponent } from './follower-x-followed-delete-dialog.component';
import { followerXFollowedRoute } from './follower-x-followed.route';

@NgModule({
  imports: [R8Meuserservice2SharedModule, RouterModule.forChild(followerXFollowedRoute)],
  declarations: [
    FollowerXFollowedComponent,
    FollowerXFollowedDetailComponent,
    FollowerXFollowedUpdateComponent,
    FollowerXFollowedDeleteDialogComponent
  ],
  entryComponents: [FollowerXFollowedDeleteDialogComponent]
})
export class R8Meuserservice2FollowerXFollowedModule {}
