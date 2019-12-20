import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { FollowerXFollowedComponent } from 'app/entities/follower-x-followed/follower-x-followed.component';
import { FollowerXFollowedService } from 'app/entities/follower-x-followed/follower-x-followed.service';
import { FollowerXFollowed } from 'app/shared/model/follower-x-followed.model';

describe('Component Tests', () => {
  describe('FollowerXFollowed Management Component', () => {
    let comp: FollowerXFollowedComponent;
    let fixture: ComponentFixture<FollowerXFollowedComponent>;
    let service: FollowerXFollowedService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [FollowerXFollowedComponent],
        providers: []
      })
        .overrideTemplate(FollowerXFollowedComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FollowerXFollowedComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FollowerXFollowedService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FollowerXFollowed(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.followerXFolloweds[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
