import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { FollowerXFollowedDetailComponent } from 'app/entities/follower-x-followed/follower-x-followed-detail.component';
import { FollowerXFollowed } from 'app/shared/model/follower-x-followed.model';

describe('Component Tests', () => {
  describe('FollowerXFollowed Management Detail Component', () => {
    let comp: FollowerXFollowedDetailComponent;
    let fixture: ComponentFixture<FollowerXFollowedDetailComponent>;
    const route = ({ data: of({ followerXFollowed: new FollowerXFollowed(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [FollowerXFollowedDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FollowerXFollowedDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FollowerXFollowedDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.followerXFollowed).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
