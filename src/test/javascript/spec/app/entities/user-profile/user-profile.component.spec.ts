import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { UserProfileComponent } from 'app/entities/user-profile/user-profile.component';
import { UserProfileService } from 'app/entities/user-profile/user-profile.service';
import { UserProfile } from 'app/shared/model/user-profile.model';

describe('Component Tests', () => {
  describe('UserProfile Management Component', () => {
    let comp: UserProfileComponent;
    let fixture: ComponentFixture<UserProfileComponent>;
    let service: UserProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [UserProfileComponent],
        providers: []
      })
        .overrideTemplate(UserProfileComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserProfileComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserProfileService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new UserProfile(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.userProfiles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
