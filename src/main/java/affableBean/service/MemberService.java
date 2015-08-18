package affableBean.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import affableBean.domain.Member;
import affableBean.domain.PasswordResetToken;
import affableBean.domain.Role;
import affableBean.domain.VerificationToken;
import affableBean.repository.MemberRepository;
import affableBean.repository.PasswordResetTokenRepository;
import affableBean.repository.RoleRepository;
import affableBean.repository.VerificationTokenRepository;
import affableBean.validation.EmailExistsException;

@Service
@Transactional
public class MemberService implements IMemberService {
    @Autowired
    private MemberRepository repository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    // API

    @Override
    public Member registerNewMemberAccount(final MemberDto accountDto) throws EmailExistsException {
        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + accountDto.getEmail());
        }
        final Member user = new Member();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setUsername(accountDto.getEmail());
        Set<Role> userRoles = new HashSet<Role>();
        userRoles.add(roleRepository.findByName("USER"));
        user.setRoles(userRoles);
        return repository.save(user);
    }

    @Override
    public Member getMember(final String verificationToken) {
        final Member user = tokenRepository.findByToken(verificationToken).getMember();
        return user;
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredMember(final Member user) {
        repository.save(user);
    }

    @Override
    public void deleteMember(final Member user) {
        repository.delete(user);
    }

    @Override
    public void createVerificationTokenForMember(final Member user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID().toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public void createPasswordResetTokenForMember(final Member user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public Member findMemberByEmail(final String email) {
        return repository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Member getMemberByPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token).getMember();
    }

    @Override
    public Member getMemberByID(final long id) {
        return repository.findOneById(id);
    }

    @Override
    public void changeMemberPassword(final Member user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        repository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final Member user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    private boolean emailExist(final String email) {
        final Member user = repository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

}