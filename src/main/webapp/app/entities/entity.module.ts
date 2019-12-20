import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-profile',
        loadChildren: () => import('./user-profile/user-profile.module').then(m => m.R8Meuserservice2UserProfileModule)
      },
      {
        path: 'rate',
        loadChildren: () => import('./rate/rate.module').then(m => m.R8Meuserservice2RateModule)
      },
      {
        path: 'comment',
        loadChildren: () => import('./comment/comment.module').then(m => m.R8Meuserservice2CommentModule)
      },
      {
        path: 'rate-x-profile',
        loadChildren: () => import('./rate-x-profile/rate-x-profile.module').then(m => m.R8Meuserservice2RateXProfileModule)
      },
      {
        path: 'comment-x-profile',
        loadChildren: () => import('./comment-x-profile/comment-x-profile.module').then(m => m.R8Meuserservice2CommentXProfileModule)
      },
      {
        path: 'follower-x-followed',
        loadChildren: () => import('./follower-x-followed/follower-x-followed.module').then(m => m.R8Meuserservice2FollowerXFollowedModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class R8Meuserservice2EntityModule {}
